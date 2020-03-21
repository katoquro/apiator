<script>
    import { isMenuOpen } from '../../../stores/single-store';
    import MenuItemList from './components/Item-list.svelte';
    import { isUpdated, markUpdated } from '../../../js/misc/cfg.js';
    import MenuIcon from './Menu-icon.svelte';

    let hasSomeUpdates = isUpdated();

    const handleCheckNews = () => {
        hasSomeUpdates = false;
        markUpdated();
    };
</script>

<div class="menu header_block_left {$isMenuOpen ? 'menu_active' : ''}">
    <MenuIcon bind:hasUpdates={hasSomeUpdates} />
    <MenuItemList
        on:check-updates={handleCheckNews}
        bind:hasUpdates={hasSomeUpdates} />
</div>
