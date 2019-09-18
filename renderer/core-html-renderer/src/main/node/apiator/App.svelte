<script>
    import { isModalVisible } from './stores/modal';

    import Header from './components/header/Header.svelte';
    import Sidebar from './components/sidebar/Sidebar.svelte';
    import Content from './components/Content.svelte';
    import Modal from './components/modal/Modal.svelte';

    const onModalWrapperClick = () => isModalVisible.set(false);

</script>

<div on:click={onModalWrapperClick} class="modal-wrapper {$isModalVisible ? 'modal-wrapper_active' : ''}">
    <Header></Header>
    <Sidebar></Sidebar>
    <Content></Content>
</div>
{#if $isModalVisible}
    <Modal></Modal>
{/if}
