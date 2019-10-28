import { writable } from 'svelte/store';

export const activeItemIndex = writable(0);
export const suggestedItems = writable([]);
export const searchValue = writable('');
