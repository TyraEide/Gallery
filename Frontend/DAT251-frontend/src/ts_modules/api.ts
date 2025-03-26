const base_url = import.meta.env.VITE_API_URL;

export function api_url(path:string){
    return base_url + path;
}

export function set_jwt_token(token:string){
    localStorage.setItem("token",token)
}

export function jwt_token_header(){
    const token     = localStorage.getItem("token")
    const headers   = new Headers({})
    headers.set("Authorization", "Bearer " + token)
    return headers
}