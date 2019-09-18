<script>
    import { splitCamelCase, getAfterLastDot } from '../services/services';
    import EndpointCard from './cards/endpoint/Endpoint.svelte';
    import EnumCard from './cards/enum/Enum.svelte';
    import TypeCard from './cards/type/Type.svelte';

    const { apiContexts, usedApiTypes, usedEnumerations } = window.apiatorJson;
    const getGroupName = (name) => splitCamelCase(getAfterLastDot(name));

</script>


<div class="content">
    {#each apiContexts as context}
        <div class="target-marker" data-id={context.apiPath}></div>
        <div class="endpoint-group">
            <div class="endpoint-group__name">
                {getGroupName(context.name)}
            </div>
            {#if context.description}
                <div class="endpoint-group__description">
                    {context.description}
                </div>
            {/if}
        {#each context.apiEndpoints as endpoint}
            <EndpointCard endpoint={endpoint} apiPath={context.apiPath} apiName={context.name}></EndpointCard>
        {/each}

        {#each usedApiTypes as type}
            <TypeCard type={type}></TypeCard>
        {/each}

        {#each usedEnumerations as enumeration}
            <EnumCard enumeration={enumeration}></EnumCard>
        {/each}
        </div>
    {/each}
</div>



