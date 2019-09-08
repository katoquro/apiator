<script>
    import {
        getIdForTargetMarkerOfModel,
        getAfterLastDot,
        getAbsoluteLinkToType,
        highlightJSON
    } from '../../../services/services';

    export let enumeration;
    let activeTab = 'standard';

    const handleTabClick = (event) => {
        if (event.target.classList.contains('card__header-view-tab')) {
            activeTab = event.target.getAttribute('data-view-tab');
        }
    };

    const { type, description, values } = enumeration;
</script>

<div class="target-marker" data-id={getIdForTargetMarkerOfModel(type)}></div>
<div class="card" data-view-tab={activeTab}>
    <div on:click={handleTabClick} class="card__header">
        <span class="card__header-name">{getAfterLastDot(type)}</span>
        <span class="card__header-share copy-btn" data-clipboard-text={getAbsoluteLinkToType(type)}>Copy link</span>

        <span class="card__header-view-tab {activeTab === 'standard' ? 'card__header-view-tab_active' : ''}" data-view-tab="standard">Standard view</span>
        <span class="card__header-view-tab {activeTab === 'tech' ? 'card__header-view-tab_active' : ''}" data-view-tab="tech">Tech view</span>
    </div>

    <div class="card__content card__content_standard">
        {#if description}
            <div class="card__content-item card__content-description">{description}</div>
        {/if}

        <div class="card__content-item">
            <div class="card__content-title">Values</div>
            <table class="table">
                <tbody>
                {#each values as value}
                    <tr>
                        <td>{value}</td>
                    </tr>
                {/each}
                </tbody>
            </table>
        </div>
    </div>

    <div class="card__content card__content_tech">
        <div class="card__content-item card__content-raw-view">
            <div class="title">Raw view</div>
            <pre class="viewer json">{@html highlightJSON(enumeration)}</pre>
        </div>
    </div>
</div>
