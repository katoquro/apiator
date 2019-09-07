<script>
    import {
        getIdForTargetMarkerOfEndpoint,
        getAbsoluteLinkToEndpoint,
        getAfterLastDot,
        renderTemplate,
        highlightJSON
    } from '../../../services/services';

    export let endpoint;
    export let apiPath;
    export let apiName;

    const { apiContexts, usedApiTypes, usedEnumerations } = window.apiatorJson;

    const { method, path, description, params, returnTypes } = endpoint;

    const idForTargetMarkerOfEndpoint = getIdForTargetMarkerOfEndpoint({
        method,
        path,
        apiPath
    });

    const linkToEndpoint = getAbsoluteLinkToEndpoint({
        method,
        path,
        apiPath
    });
</script>

<div class="target-marker" data-id={idForTargetMarkerOfEndpoint}></div>
<div class="card endpoint endpoint_type_{endpoint.method}" data-view-tab="standard">
    <div class="card__header">
        <span class="card__header-name">{method}</span>
        <span class="card__header-name">{apiPath}{path}</span>
        <span class="card__header-share copy-btn" data-clipboard-text={linkToEndpoint}>Copy link</span>
        <span class="card__header-view-tab card__header-view-tab_active" data-view-tab="standard">Standard view</span>
        <span class="card__header-view-tab" data-view-tab="tech">Tech view</span>
    </div>

    <div class="card__content card__content_standard">
        {#if description}
            <div class="card__content-item card__content-description">{description}</div>
        {/if}

        {#if params}
            <div class="card__content-item">
                <div class="card__content-title">Parameters</div>
                <table class="table table_endpoint">
                    <colgroup>
                        <col width="25%">
                        <col width="20%">
                        <col width="25%">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Parameter type</th>
                        <th>Data type</th>
                        <th>Default value</th>
                        <th>Optional</th>
                    </tr>
                    </thead>
                    <tbody>
                    {#each params as param}
                        <tr class="param">
                            <td>
                                {param.name}
                                {#if param.required}
                                    <span class="param__required">(required)</span>
                                {/if}
                            </td>

                            <td class="param__http-type">{param.httpParamType}</td>
                                <td>{@html renderTemplate(param)}</td>
                            <td>
                                {#if param.defaultValue}
                                    <span class="param__default-value">{param.defaultValue}</span>
                                {:else}
                                    -
                                {/if}
                            </td>
                            <td>
                                {#if param.optional}
                                    <span class="param__optional">&#x2713;</span>
                                {:else}
                                    -
                                {/if}
                            </td>
                        </tr>
                    {/each}
                    </tbody>
                </table>
            </div>
        {/if}

        <div class="card__content-item">
            <div class="card__content-title">Responses</div>
            {#each returnTypes as type}
                {@html renderTemplate(type)}
            {/each}
        </div>
    </div>

    <div class="card__content card__content_tech">
        <div class="card__content-item card__content-endpoint-method">
            <div class="title">Java method</div>
            <div class="name">{getAfterLastDot(apiName)}.{endpoint.name}</div>
            <div class="copy copy-btn"
                 data-clipboard-text="{{apiName}}#{{name}}">copy reference
            </div>
        </div>
        <div class="card__content-item card__content-raw-view">
            <div class="title">Raw view</div>
            <pre class="viewer json">{@html highlightJSON(endpoint)}}}</pre>
        </div>
    </div>
</div>

