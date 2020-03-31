import api from '../components/Axios/Axios'

export const saveFile = async (url, filename) => {
  api.getBlob(url, null)
    .then(res => save(res, filename))
}

export const save = (blob, filename) => {
  const a = document.createElement('a')
  document.body.appendChild(a)
  a.style = 'display: none'

  const url = window.URL.createObjectURL(blob)
  a.href = url
  a.download = filename
  a.click()
  window.URL.revokeObjectURL(url)
}