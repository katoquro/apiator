<script>
    import { getPageLinkToType, getAfterLastDot } from '../../../services/services';

    export let type;

    function handleClick() {
        location.hash = getPageLinkToType(type.type);
    }
</script>

<li class="api">
    <span on:click={handleClick} class="api__title {type.modelType}">
        {getAfterLastDot(type.type)}
    </span>
</li>
