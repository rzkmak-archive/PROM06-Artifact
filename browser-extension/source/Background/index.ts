import {browser} from 'webextension-polyfill-ts';
import {Md5} from 'ts-md5'
import axios from "axios";

const TWITTER_URL_REGEX = /^https?:\/\/(?:[^./?#]+\.)?twitter\.com/;

browser.runtime.onInstalled.addListener((): void => {
    console.log('extension installed');

    const createHashLookupKeyForTweet = (message: string): string => {
        if (!message) return message
        return Md5.hashStr(message)
    }

    const sendTweetsToBackend = (sessionId: string, message: string) => {
        let hashLookUp = createHashLookupKeyForTweet(message)
        console.log("hashLookup", hashLookUp)
        browser
            .storage
            .local
            .get([hashLookUp])
            .then(hashValue => {
                if (hashValue[hashLookUp] == null) {
                    axios.post(
                        'http://localhost:8080/v1/self/cyberbully/screening/twitter',
                        {
                            "tweetValue": message
                        },
                        {
                            headers: {
                                'Content-Type': 'application/json',
                                'X-Session-Id': sessionId
                            }
                        }
                    ).then(r => {
                        console.log("response from backend", r)
                        if (r.status != 202) {
                            console.error("error when publishing tweets: ", r.data)
                        }
                        return r.data["sessionId"]
                    })
                    browser.storage.local.set({[hashLookUp]: true})
                }
            })
    }

    setInterval(() => {
        browser.storage.sync.get(
            "CYBER_BULLY_EXTENSION_USER_ID_KEY"
        ).then(sessionId => {
            browser
                .tabs
                .query({currentWindow: true, active: true})
                .then((tabs) => {
                        let tab = tabs[0]; // Safe to assume there will only be one result
                        if (tab && !TWITTER_URL_REGEX.test(tab.url!)) {
                            console.log("skip tab is not target")
                        } else {
                            if (!tab) {
                                console.log("tab is inactive, skipping")
                            } else {
                                browser
                                    .tabs
                                    .sendMessage(tab.id!, {text: 'dom_pass'})
                                    .then(result => {
                                        Array
                                            .from(result)
                                            .forEach(r => {
                                                if (typeof r === "string") {
                                                    sendTweetsToBackend(sessionId["CYBER_BULLY_EXTENSION_USER_ID_KEY"], r)
                                                }
                                            });
                                    })
                            }
                        }
                    }
                    ,
                    console.error
                )
            console.log("enable polling for: ", sessionId["CYBER_BULLY_EXTENSION_USER_ID_KEY"])
        }).catch(() => {
            console.log("skip polling")
        })
    }, 3000)
});

browser.runtime.onStartup.addListener((): void => {
    console.log('extension started')
});

browser.tabs.onUpdated.addListener((): void => {
    console.log('page updated')
});

