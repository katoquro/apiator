<script>
    import {
        getIdForTargetMarkerOfModel,
        getAfterLastDot,
        getAbsoluteLinkToType,
        renderTemplate,
        highlightJSON,
    } from '../../../services/services';

    export let type;

    let activeTab = 'standard';

    const handleTabClick = event => {
        if (event.target.classList.contains('card__header-view-tab')) {
            activeTab = event.target.getAttribute('data-view-tab');
        }
    };

    const { description, fields } = type;
</script>

<div class="target-marker" data-id={getIdForTargetMarkerOfModel(type.type)} />
<div class="card type" data-view-tab={activeTab}>
    <div on:click={handleTabClick} class="card__header">
        <span class="card__header-name">{getAfterLastDot(type.type)}</span>
        <span
            class="card__header-share copy-btn"
            data-clipboard-text={getAbsoluteLinkToType(type.type)}>
            Copy link
        </span>

        <span
            class="card__header-view-tab {activeTab === 'standard' ? 'card__header-view-tab_active' : ''}"
            data-view-tab="standard">
            Standard view
        </span>
        <span
            class="card__header-view-tab {activeTab === 'tech' ? 'card__header-view-tab_active' : ''}"
            data-view-tab="tech">
            Tech view
        </span>
    </div>

    <div class="card__content card__content_standard">
        {#if description}
            <div class="card__content-item card__content-description">
                {description}
            </div>
        {/if}

        <div class="card__content-item">
            <div class="card__content-title">Fields</div>
            <table class="table">
                <colgroup>
                    <col width="35%" />
                    <col width="35%" />
                </colgroup>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Date type</th>
                        <th>Default value</th>
                        <th>Optional</th>
                    </tr>
                </thead>
                <tbody>
                    {#each fields as field}
                        <tr>
                            <td>{field.name}</td>
                            <td>
                                {@html renderTemplate(field)}
                            </td>
                            <td>
                                {#if field.defaultValue}
                                    <span class="param__default-value">
                                        {field.defaultValue}
                                    </span>
                                {:else}-{/if}
                            </td>
                            <td>
                                {#if field.optional}
                                    <span class="param__optional">
                                        &#x2713;
                                    </span>
                                {:else}-{/if}
                            </td>
                        </tr>
                    {/each}
                </tbody>
            </table>
        </div>

        <div class="card__content-item">

            <div class="card__content-title">Source Reference</div>
            <div class="type__src-ref">
                <div class="type__src-ref-name">{type.type}</div>

                <div
                    class="type__src-ref-copy copy-btn"
                    data-clipboard-text={type.type}>
                    copy reference
                </div>
            </div>
        </div>

    </div>

    <div class="card__content card__content_tech">
        <div class="card__content-item card__content-raw-view">
            <div class="title">Raw view</div>
            <pre class="viewer json">
                {@html highlightJSON(type)}
            </pre>
        </div>
    </div>

</div>
