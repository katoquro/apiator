<script>
    import {
        apiContexts,
        usedApiTypes,
        usedEnumerations,
    } from '../../js/apiator-data';

    import ContextsItem from './components/Contexts-item.svelte';
    import TypeItem from './components/Type-item.svelte';
    import EnumItem from './components/Enum-item.svelte';
    import EndpointIcon from './icons/endpoint.svg';
    import ModelIcon from './icons/model.svg';

    const handleGroupTitleClick = event => {
        event.currentTarget.nextElementSibling.classList.toggle(
            'group__content_active'
        );
    };
</script>

<style>
    .sidebar__group_icon {
        width: 16px;
    }
</style>

<div class="sidebar">
    <ul class="sidebar__group">
        <div
            on:click={handleGroupTitleClick}
            class="group__title js_sidebar-title-endpoints">
            <i
                style="background-image: url({EndpointIcon})"
                class="sidebar__group_icon" />
            <span>Endpoints</span>
        </div>
        <li class="group__content group__content_active js_sidebar-endpoints">
            <ul>
                {#each apiContexts as context}
                    <ContextsItem {context} />
                {/each}
            </ul>
        </li>

        <div
            on:click={handleGroupTitleClick}
            class="group__title js_sidebar-title-model">
            <i
                style="background-image: url({ModelIcon})"
                class="sidebar__group_icon" />
            <span>Types</span>
        </div>
        <li class="group__content js_sidebar-model">
            <ul>
                {#each usedApiTypes as type}
                    <TypeItem {type} />
                {/each}
                {#each usedEnumerations as enumeration}
                    <EnumItem {enumeration} />
                {/each}
            </ul>
        </li>
    </ul>
</div>
