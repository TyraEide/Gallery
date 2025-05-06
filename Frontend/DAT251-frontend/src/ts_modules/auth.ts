import {writable} from "svelte/store";
import type {UUID} from "node:crypto";
import {redirect} from "./routing";

export interface User {id: UUID, username: string, email: string}
export let user= writable<User | null>(null);

export function login(userData: User) {
    localStorage.setItem("user", JSON.stringify(userData));
    user.set(userData);
}

export function logout() {
    localStorage.clear();
    user.set(null);
    redirect("");
}