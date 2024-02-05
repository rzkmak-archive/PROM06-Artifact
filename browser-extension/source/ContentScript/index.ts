import {browser} from "webextension-polyfill-ts";

browser.runtime.onMessage.addListener((message): Promise<any> => {
    if (message.text == 'dom_pass') {
        const tweets =
            Array
                .from(document.querySelectorAll("[data-testid='tweetText'] > span"))
                .map(e => e.textContent)

        return Promise.resolve(tweets)
    }
    return Promise.reject()
})
