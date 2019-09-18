import App from './App.svelte';

export default function () {
    return new App({
        target: document.querySelector('#doc-container')
    });
}
