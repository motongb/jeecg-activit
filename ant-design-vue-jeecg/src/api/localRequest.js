import { axios } from '@/utils/request'

//post
export function postFormAction(url, parameter) {
  return axios({
    url: url,
    method: 'post',
    params: parameter
  })
}
