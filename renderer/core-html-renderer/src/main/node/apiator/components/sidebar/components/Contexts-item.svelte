<script>
    import EndpointsList from './Endpoints-list.svelte';

    export let context;

    let isActiveItem = false;

    const toggleActiveItem = () => {
        debugger;
        isActiveItem = !isActiveItem;
    };

    const handleApiTitleClick = () => {
        if (isActiveItem) {
            location.hash = `#${context.apiPath}`;
        }
    };
</script>

<li on:click={toggleActiveItem} class="api {isActiveItem ? 'api_active' : ''}">
    <div class="api__header">
        <span on:click={handleApiTitleClick} class="api__title">
            {context.apiPath}
        </span>
        <div class="api__toggle"></div>
    </div>

    <EndpointsList apiPath={context.apiPath} endpoints={context.apiEndpoints}></EndpointsList>
</li>
