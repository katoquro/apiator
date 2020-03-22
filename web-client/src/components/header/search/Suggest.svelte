<script>
    import { activeItemIndex, suggestedItems } from './stores';
    import SuggestItem from './Suggest-item.svelte';

    let items = [];

    suggestedItems.subscribe(value => {
        items = value;
    });
</script>

<ul class="search__suggest">
    {#each items as item, index (item)}
        <SuggestItem
            on:mouseOverForward={() => activeItemIndex.set(index)}
            isActive={$activeItemIndex === index}
            item={item.payload} />
    {/each}
</ul>
