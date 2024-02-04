import {browser} from 'webextension-polyfill-ts';

browser.runtime.onInstalled.addListener((): void => {
  console.log('🦄', 'extension installed');
});

browser.runtime.onStartup.addListener((): void => {
  console.log('extension started')
});

browser.tabs.onUpdated.addListener((): void => {
  console.log('page updated')
});
