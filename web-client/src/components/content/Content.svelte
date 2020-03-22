<script>
    import {
        apiContexts,
        usedApiTypes,
        usedEnumerations,
    } from '../../js/apiator-data.js';
    import { splitCamelCase, getAfterLastDot } from '../../services/services';
    import EndpointCard from '../cards/endpoint/Endpoint.svelte';
    import EnumCard from '../cards/enum/Enum.svelte';
    import TypeCard from '../cards/type/Type.svelte';

    const getGroupName = name => splitCamelCase(getAfterLastDot(name));
</script>

<div class="content">
    {#each apiContexts as context}
        <div class="target-marker" data-id={context.apiPath} />
        <div class="endpoint-group">
            <div class="endpoint-group__name">{getGroupName(context.name)}</div>
            {#if context.description}
                <div class="endpoint-group__description">
                    {context.description}
                </div>
            {/if}
            {#each context.apiEndpoints as endpoint}
                <EndpointCard
                    {endpoint}
                    apiPath={context.apiPath}
                    apiName={context.name} />
            {/each}
        </div>
    {/each}
    {#each usedApiTypes as type}
        <TypeCard {type} />
    {/each}

    {#each usedEnumerations as enumeration}
        <EnumCard {enumeration} />
    {/each}
</div>
