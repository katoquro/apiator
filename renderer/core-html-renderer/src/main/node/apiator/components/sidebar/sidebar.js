import Sidebar from './Sidebar.svelte';

export default function () {
    return new Sidebar({
        target: document.querySelector('.sidebar')
    });
}
