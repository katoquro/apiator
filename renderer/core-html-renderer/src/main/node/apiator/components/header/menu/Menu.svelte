<script>
    import MenuItemList from './components/Item-list.svelte';
    import { isUpdated, markUpdated } from '../../../js/misc/cfg.js';
    import MenuIcon from './Menu-icon.svelte';

    let isOpen = false;
    let hasSomeUpdates = isUpdated();

    const handleCheckNews = () => {
        isOpen = false;
        hasSomeUpdates = false;
        markUpdated();
    };

</script>

<div class="menu header_block_left {isOpen ? 'menu_active' : ''}">
    <MenuIcon bind:hasUpdates={hasSomeUpdates} bind:menuState={isOpen}/>
    <MenuItemList on:check-updates={handleCheckNews} bind:hasUpdates={hasSomeUpdates}/>
</div>
