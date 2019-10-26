<script>
    import { isModalVisible } from '../../stores/single-store';

    const onClick = () => isModalVisible.set(false);
</script>

<div on:click={onClick} class="modal">
    <div class="modal__content">
        <div class="modal__content-header">Thanks &#x1F495;</div>
        <div class="modal__content-body">
            <p>
                <em>Darya Herasimovich</em>
                for design and product vision. We haven't done much cool
                features in this release but we return to them in the future!
            </p>
            <p>
                <em>
                    <a href="https://github.com/elipskiy" target="_blank">
                        Egor Lipskiy
                    </a>
                </em>
                for js/html magic in severe backend environment. It was hard and
                curious!
            </p>
        </div>
    </div>
</div>
