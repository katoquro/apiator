<script>
    import { isModalVisible } from '../../../../stores/single-store';
    import { createEventDispatcher } from 'svelte';
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
        <svg
            aria-hidden="true"
            data-prefix="fas"
            data-icon="gift"
            class="svg-inline--fa fa-gift fa-w-16 menu__items-item-icon"
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 512 512">
            <path
                fill={hasUpdates ? '#cd7778' : '#838688'}
                d="M32 448c0 17.7 14.3 32 32 32h160V320H32v128zm256 32h160c17.7
                0 32-14.3 32-32V320H288v160zm192-320h-42.1c6.2-12.1 10.1-25.5
                10.1-40 0-48.5-39.5-88-88-88-41.6 0-68.5 21.3-103
                68.3-34.5-47-61.4-68.3-103-68.3-48.5 0-88 39.5-88 88 0 14.5 3.8
                27.9 10.1 40H32c-17.7 0-32 14.3-32 32v80c0 8.8 7.2 16 16
                16h480c8.8 0 16-7.2 16-16v-80c0-17.7-14.3-32-32-32zm-326.1
                0c-22.1 0-40-17.9-40-40s17.9-40 40-40c19.9 0 34.6 3.3 86.1
                80h-86.1zm206.1 0h-86.1c51.4-76.5 65.7-80 86.1-80 22.1 0 40 17.9
                40 40s-17.9 40-40 40z" />
        </svg>
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
