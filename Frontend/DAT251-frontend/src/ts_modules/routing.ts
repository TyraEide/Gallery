export function redirect(subPath:string=""){
    window.location.href = window.location.origin + '#/' + subPath;
}