<script>
    import { activeItemIndex, suggestedItems, searchValue } from './search/stores';
    import Menu from './menu/Menu.svelte';
    import Suggest from './search/Suggest.svelte';
    import { onChange, search } from './search/search';

    const KEYS = {
        UP: 38,
        DOWN: 40,
        ENTER: 13
    };

    function searchOnInput() {
        suggestedItems.set([]);

        if (!$searchValue) {
            return;
        }

        search($searchValue).forEach((item, index) => {
            suggestedItems.update(arr => {
                arr.push(item);

                return arr;
            });
        });
    }

    function handleKeyUp(event) {
        if (KEYS[event.keyCode]) {
            return;
        }

        searchOnInput();
    }

    function handleKeyDown(event) {
        switch (event.keyCode) {
            case KEYS.ENTER: {
                onChange(event.keyCode, $suggestedItems[$activeItemIndex]);

                break;
            }
            case KEYS.UP:
                activeItemIndex.update(index => index - 1);

                break;
            case KEYS.DOWN: {
                activeItemIndex.update(index => index + 1);

                break;
            }
            default:
                return;
        }

        event.preventDefault();
        event.stopPropagation();

        return false;
    }

    function handleClick() {
        if (!$searchValue) {
            searchOnInput();
        }
    }


</script>

<div class="header">
    <Menu/>
    <div class="header_block_right">
        <form class="search" role="search">
            <input
                    on:keyup={handleKeyUp}
                    on:keydown={handleKeyDown}
                    on:click={handleClick}
                    bind:value={$searchValue}
                    type="text"
                    class="search__input"
                    id="fuzzy-input"
                    autocomplete="off"
                    placeholder="Search or start with '!'">
            {#if $suggestedItems.length}
                <Suggest items={$suggestedItems}></Suggest>
            {/if}
        </form>
    </div>
</div>
