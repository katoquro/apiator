<script>
    import { isMenuOpen } from '../../../stores/single-store';
    import Icon from './icon.svg';

    export let hasUpdates;

    const handleClick = () => isMenuOpen.set(!$isMenuOpen);
</script>

<div
    on:click={handleClick}
    style="background-image: url({Icon})"
    class="menu__pic {hasUpdates ? 'menu__pic_notification' : ''}" />
