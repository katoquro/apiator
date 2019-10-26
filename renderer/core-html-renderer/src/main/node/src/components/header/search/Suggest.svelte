<script>
    import { activeItemIndex } from './stores';
    import SuggestItem from './Suggest-item.svelte';

    export let items;

    const handleMouseOver = index => () => activeItemIndex.set(index);
</script>

<ul class="search__suggest">
    {#each items as item, index (item.index)}
        <SuggestItem
            on:mouseOverForward={handleMouseOver(index)}
            isActive={$activeItemIndex === index}
            item={item.payload} />
    {/each}
</ul>
