<script>
    import { isModalVisible, isMenuOpen } from './stores/single-store';

    import Header from './components/header/Header.svelte';
    import Sidebar from './components/sidebar/Sidebar.svelte';
    import Content from './components/content/Content.svelte';
    import Modal from './components/modal/Modal.svelte';

    const onModalWrapperClick = (event) => {
        isMenuOpen.set(false);
        isModalVisible.set(false);
    }

</script>

<div on:click|capture={onModalWrapperClick} class="modal-wrapper {$isModalVisible ? 'modal-wrapper_active' : ''}">
    <Header></Header>
    <Sidebar></Sidebar>
    <Content></Content>
</div>
{#if $isModalVisible}
    <Modal></Modal>
{/if}
