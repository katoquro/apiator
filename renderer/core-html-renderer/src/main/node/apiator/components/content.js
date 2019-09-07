import Content from './Content.svelte';

export default function () {
    return new Content({
        target: document.querySelector('.content')
    });
}
