<script>
    import {
        activeItemIndex,
        suggestedItems,
        searchValue,
    } from './search/stores';
    import Menu from './menu/Menu.svelte';
    import Suggest from './search/Suggest.svelte';
    import { onChange, search } from './search/search';

    const KEYS = {
        UP: 38,
        DOWN: 40,
        ENTER: 13,
    };

    function searchOnInput() {
        suggestedItems.set([]);

        if (!$searchValue) {
            return;
        }
    }

    searchValue.subscribe(value => {
        suggestedItems.set(search(value));
    });

    function handleKeyUp(event) {
        if (KEYS[event.keyCode]) {
            return;
        }
    }

    function handleKeyDown(event) {
        switch (event.keyCode) {
            case KEYS.ENTER: {
                onChange(
                    event.keyCode,
                    $suggestedItems[$activeItemIndex].payload
                );

                break;
            }
            case KEYS.UP:
                if ($activeItemIndex > 0) {
                    activeItemIndex.update(index => index - 1);
                }

                break;
            case KEYS.DOWN: {
                if ($activeItemIndex < $suggestedItems.length - 1) {
                    activeItemIndex.update(index => index + 1);
                }

                break;
            }
            default:
                activeItemIndex.set(0);

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
    <Menu />
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
                placeholder="Search or start with '!'"
                autofocus />
            {#if $suggestedItems.length}
                <Suggest />
            {/if}
        </form>
    </div>
</div>
