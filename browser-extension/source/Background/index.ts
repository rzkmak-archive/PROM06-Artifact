import {browser} from 'webextension-polyfill-ts';
import {Md5} from 'ts-md5'

const TWITTER_URL_REGEX = /^https?:\/\/(?:[^./?#]+\.)?twitter\.com/;

browser.runtime.onInstalled.addListener((): void => {
    console.log('extension installed');

    const createHashLookupKeyForTweet = (message: string): string => {
        if (!message) return message
        return Md5.hashStr(message)
    }

    const sendTweetsToBackend = (message: string) => {
        let hashLookUp = createHashLookupKeyForTweet(message)
        browser
            .storage
            .local
            .get(hashLookUp)
            .catch(() => {
                console.log("item will be processed", hashLookUp)
                browser.storage.local.set({[hashLookUp]: true})
            })
    }

    setInterval(() => {
        browser.storage.local.get(
            "CYBER_BULLY_EXTENSION_USER_ID_KEY"
        ).then(result => {
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
                                                    sendTweetsToBackend(r)
                                                }
                                            });
                                    })
                            }
                        }
                    }
                    ,
                    console.error
                )
            console.log("enable polling for: ", result["CYBER_BULLY_EXTENSION_USER_ID_KEY"])
        }).catch(() => {
            console.log("skip polling")
        })
    }, 1000)
});

browser.runtime.onStartup.addListener((): void => {
    console.log('extension started')
});

browser.tabs.onUpdated.addListener((): void => {
    console.log('page updated')
});

