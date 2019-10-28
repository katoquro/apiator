describe('Search', () => {
    beforeEach(() => {
        cy.visit('/');
    });

    it('should focus search input on load', () => {
        cy.focused().should('have.class', 'search__input');
    });

    it('should accept input', () => {
        const inputText = '/books';

        cy.get('.search__input')
            .type(inputText)
            .should('have.value', inputText);
    });

    it('should show search suggest', () => {
        cy.get('.search__suggest').should('not.exist');

        cy.get('.search__input').type('/books');

        cy.get('.search__suggest').should('exist');
    });

    it('should click first search suggest result and scroll to relevant card', () => {
        cy.get('.card__header-name')
            .contains('/books')
            .should('not.be.visible');
        cy.get('.search__input').type('/books');
        cy.get('.search__suggest li:first').click();

        cy.wait(1000);

        cy.get('.card__header-name')
            .contains('/books')
            .should('be.visible');
    });

    it('should search through url addresses using !endpoint flag', () => {
        cy.get('[data-id="1/e/_get_/magazine/"]').should('not.be.visible');

        cy.get('.search__input').type('!endpoint /magazine/');
        cy.get('.search__suggest li:first').click();

        cy.wait(1000);

        cy.get('[data-id="1/e/_get_/magazine/"]').should('be.visible');
    });

    it('should search through model and enum names using !model flag', () => {
        cy.get('[data-id="1/m/Magazine"]').should('not.be.visible');

        cy.get('.search__input').type('!model Magazine');
        cy.get('.search__suggest li:first').click();

        cy.wait(1000);

        cy.get('[data-id="1/m/Magazine"]').should('be.visible');
    });
});
