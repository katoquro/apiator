<script>
    import { getPageLinkToEndpoint } from '../../../services/services';

    export let endpoints;
    export let apiPath;

    const onClick = (endpoint) => () => {
        location.hash = getPageLinkToEndpoint({...endpoint, apiPath});
    };

</script>

<ul class="api__endpoints">
    {#each endpoints as endpoint}
        <li on:click|stopPropagation={onClick(endpoint)}>
            <span title={endpoint.path} data-link={getPageLinkToEndpoint({...endpoint, apiPath})}>
                <span class='api__endpoints-{endpoint.method}'>
                    {endpoint.method} {endpoint.path}
                </span>
            </span>
        </li>
    {/each}
</ul>
