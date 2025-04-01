export function redirect(subPath:string=""){
    window.location.href = window.location.origin + '#/' + subPath;
}

export function build_page_url(subPath:string=""){
    return window.location.origin + '#/' + subPath;
}