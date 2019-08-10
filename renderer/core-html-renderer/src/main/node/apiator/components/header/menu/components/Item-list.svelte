<script>
    import { showModal } from '../../../../js/misc/modal.js'
    import { createEventDispatcher } from 'svelte';

    export let hasUpdates;

    const dispatch = createEventDispatcher();
    const handleWhatsNewItemClick = () => dispatch('check-updates');
    const handleThanksItemClick = () => showModal();
</script>

<ul class="menu__items">
    <li on:click={handleWhatsNewItemClick} class="menu__items-item js__menu-item-whats-new {hasUpdates ? 'menu__items-item_notification' : ''}">
        <i class="menu__items-item-icon fa fa-gift" aria-hidden="true"></i>
        <a href="https://github.com/ainrif/apiator/blob/develop/CHANGELOG.md" target="_blank"
           class="menu__items-item-link">What's New</a>
    </li>
    <li class="menu__items-item">
        <i class="menu__items-item-icon fa fa-book" aria-hidden="true"></i>
        <a href="https://apiator.ainrif.com" target="_blank"
           class="menu__items-item-link">Documentation</a>
    </li>
    <li class="menu__items-item">
        <i class="menu__items-item-icon fa fa-github" aria-hidden="true"></i>
        <a href="https://github.com/ainrif/apiator" target="_blank"
           class="menu__items-item-link">Source code</a>
    </li>
    <li class="menu__items-item">
        <i class="menu__items-item-icon fa fa-bug" aria-hidden="true"></i>
        <a href="https://github.com/ainrif/apiator/issues" target="_blank"
           class="menu__items-item-link">Report an Issue</a>
    </li>
    <li on:click={handleThanksItemClick} class="menu__items-item js__menu-item-thanks">
        <i class="menu__items-item-icon fa fa-heart" aria-hidden="true"></i>
        <span>Thanks</span>
    </li>
</ul>
