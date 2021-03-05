import { getAction, postAction } from './manage'

const api = {
  createNewFileUrl: '/base/webOffice/getNewFileUrl',
  preViewUrl: '/base/webOffice/getViewUrl',
  saveWpsModel: '/base/webOffice/saveWpsModel'
}

export const createNewFile = (fileType) => {
  if (!fileType) {
    fileType = 'word'
  }
  return getAction(api.createNewFileUrl, { fileType })
}

export const getPreViewUrl = (fileId, fileType) => {
  if (!fileType) {
    fileType = 'word'
  }
  return getAction(api.preViewUrl, { fileId, fileType })
}

export const saveWpsModel = (data) => {
  return postAction(api.saveWpsModel, data)
}