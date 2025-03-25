const base_url = import.meta.env.VITE_API_URL;

export function api_url(path:string){
    return base_url + path;
}