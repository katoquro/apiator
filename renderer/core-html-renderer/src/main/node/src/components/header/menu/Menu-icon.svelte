<script>
    import { isMenuOpen } from '../../../stores/single-store';
    import Icon from './bw_favicon.png';

    export let hasUpdates;

    const handleClick = () => isMenuOpen.set(!$isMenuOpen);
</script>

<div
    on:click={handleClick}
    style="background-image: url(data:image/png;base64,{Icon})"
    class="menu__pic {hasUpdates ? 'menu__pic_notification' : ''}" />
