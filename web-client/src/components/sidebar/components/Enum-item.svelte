<script>
    import {
        getPageLinkToType,
        getAfterLastDot,
    } from '../../../services/services';

    export let enumeration;

    function handleClick() {
        location.hash = getPageLinkToType(enumeration.type);
    }
</script>

<li class="api">
    <span on:click={handleClick} class="api__title {enumeration.modelType}">
        {getAfterLastDot(enumeration.type)}
    </span>
</li>
