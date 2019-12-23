<script>
    import { isModalVisible } from '../../../../stores/single-store';
    import { createEventDispatcher } from 'svelte';
    import WhatsNewIcon from '../icons/gift-solid.svg';
    import DocumentationIcon from '../icons/book-solid.svg';
    import SourceIcon from '../icons/github-alt-brands.svg';
    import ReportBugIcon from '../icons/bug-solid.svg';
    import ThanksIcon from '../icons/heart-solid.svg';

    export let hasUpdates;

    const dispatch = createEventDispatcher();
    const handleWhatsNewItemClick = () => dispatch('check-updates');
    const handleThanksItemClick = () => isModalVisible.set(true);
</script>

<style>
    .menu__items-item-icon {
        width: 16px;
        height: 16px;
    }
    .menu__items-item > .menu__items-item-link {
        padding-left: 15px;
    }
</style>

<ul class="menu__items">
    <li
        on:click={handleWhatsNewItemClick}
        class="menu__items-item js__menu-item-whats-new {hasUpdates ? 'menu__items-item_notification' : ''}">
        <i
            class="menu__items-item-icon"
            style="background: url({WhatsNewIcon}) no-repeat" />
        <a
            href="https://github.com/ainrif/apiator/blob/develop/CHANGELOG.md"
            target="_blank"
            class="menu__items-item-link">
            What's New
        </a>
    </li>
    <li class="menu__items-item">
        <i
            class="menu__items-item-icon"
            style="background: url({DocumentationIcon}) no-repeat" />
        <a
            href="https://docs.apiator.info"
            target="_blank"
            class="menu__items-item-link">
            Documentation
        </a>
    </li>
    <li class="menu__items-item">
        <i
            class="menu__items-item-icon"
            style="background: url({SourceIcon}) no-repeat" />
        <a
            href="https://github.com/ainrif/apiator"
            target="_blank"
            class="menu__items-item-link">
            Source code
        </a>
    </li>
    <li class="menu__items-item">
        <i
            class="menu__items-item-icon"
            style="background: url({ReportBugIcon}) no-repeat" />
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
        <i
            class="menu__items-item-icon"
            style="background: url({ThanksIcon}) no-repeat" />
        <span class="menu__items-item-link">Thanks</span>
    </li>
</ul>
