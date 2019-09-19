<script>
    import { isMenuOpen } from '../../../stores/single-store';
    import Icon from './bw_favicon.webp';

    export let hasUpdates;

    const icon = `data:image/png;base64,${Icon}`;

    const handleClick = () => isMenuOpen.set(!$isMenuOpen);
</script>

<div
    on:click={handleClick}
    style="background-image: url({Icon})"
    class="menu__pic {hasUpdates ? 'menu__pic_notification' : ''}">
</div>
