import Header from './Header.svelte';

export default function menu() {
    return new Header({
        target: document.querySelector('.header')
    });
}
