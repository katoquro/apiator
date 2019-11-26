<script>
    import { isModalVisible } from '../../../../stores/single-store';
    import { createEventDispatcher } from 'svelte';

    export let hasUpdates;

    const dispatch = createEventDispatcher();
    const handleWhatsNewItemClick = () => dispatch('check-updates');
    const handleThanksItemClick = () => isModalVisible.set(true);
</script>

<ul class="menu__items">
    <li
        on:click={handleWhatsNewItemClick}
        class="menu__items-item js__menu-item-whats-new {hasUpdates ? 'menu__items-item_notification' : ''}">
        <a
            href="https://github.com/ainrif/apiator/blob/develop/CHANGELOG.md"
            target="_blank"
            class="menu__items-item-link">
            What's New
        </a>
    </li>
    <li class="menu__items-item">
        <a
            href="https://docs.apiator.info"
            target="_blank"
            class="menu__items-item-link">
            Documentation
        </a>
    </li>
    <li class="menu__items-item">
        <a
            href="https://github.com/ainrif/apiator"
            target="_blank"
            class="menu__items-item-link">
            Source code
        </a>
    </li>
    <li class="menu__items-item">
        <a
            href="https://github.com/ainrif/apiator/issues"
            target="_blank"
            class="menu__items-item-link">
            Report an Issue
        </a>
    </li>
    <li
        on:click|stopPropagation={handleThanksItemClick}
        class="menu__items-item js__menu-item-thanks">
        <span>Thanks</span>
    </li>
</ul>
