<script>
    import Icon from './bw_favicon.webp';

    export let menuState;
    export let hasUpdates;

    const icon = `data:image/png;base64,${Icon}`;

    const handleClick = () => {
        menuState = !menuState;
    }
</script>

<div
    on:click={handleClick}
    style="background-image: url({Icon})"
    class="menu__pic {hasUpdates ? 'menu__pic_notification' : ''}">
</div>
