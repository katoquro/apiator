<script>
    import ContextsItem from './components/Contexts-item.svelte';
    import TypeItem from './components/Type-item.svelte';
    import EnumItem from './components/Enum-item.svelte';

    const { apiContexts, usedApiTypes, usedEnumerations } = window.apiatorJson;

    const handleGroupTitleClick = event => {
        event.currentTarget.nextElementSibling.classList.toggle(
            'group__content_active'
        );
    };
</script>

<div class="sidebar">
    <ul class="sidebar__group">
        <div
            on:click={handleGroupTitleClick}
            class="group__title js_sidebar-title-endpoints">
            <i class="fa fa-plug" />
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
            <i class="fa fa-puzzle-piece" />
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
