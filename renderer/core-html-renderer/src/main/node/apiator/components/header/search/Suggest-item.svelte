<script>
    import { createEventDispatcher } from 'svelte';
    import { onChange } from './search';
    import { getPageLinkToEndpoint, getPageLinkToType } from '../../../services/services';

    export let item;
    export let isActive;

    const {
        showAs,
        method,
        apiPath,
        path,
        simpleName,
        enclosingType,
        name,
        description
    } = item;

    const dispatch = createEventDispatcher();

    const isEndpoint = showAs === 'endpoint';
    const isModel = showAs === 'model';
    const isEnum = showAs === 'enum';
    const isBang = showAs === 'bang';

    const handleMouseOver = (event) => dispatch('mouseOverForward', {});

</script>

<li on:click={() => onChange(null, item)} on:mouseover={handleMouseOver} class="search__suggest-item {isActive ? 'search__suggest-item_active' : ''}">
    {#if isEndpoint}
        <a data-link={getPageLinkToEndpoint(item)}>
            <span class="api__endpoints-{method}">
                {method} {apiPath}{path}
            </span>
        </a>
    {/if}
    {#if isModel}
        <a data-link={getPageLinkToType(item)}>
            <span class="endpoint">{simpleName}</span>
        </a>
    {/if}
    {#if isEnum}
        <a data-link={getPageLinkToType(item)}>
            <span class="endpoint">{simpleName}
                {#if enclosingType}
                    at {enclosingType}
                {/if}
                <b>(enumeration)</b>
            </span>
        </a>
    {/if}
    {#if isBang}
        <span>
            <em><b>!</b>{name}</em> - {description}
        </span>
    {/if}
</li>
