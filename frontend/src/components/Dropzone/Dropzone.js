import Dropzone from 'react-dropzone'
import React from 'react'

export const ReactDropzone = props => {
  const {onDrop} = props

  return (
    <Dropzone
      disabled={props.disabled}
      onDrop={onDrop}>
      {({getRootProps, getInputProps}) => (
        <section>
          <div {...getRootProps()}>
            <input {...getInputProps()} />
            <p>Drag 'n' drop some files here, or click to select files</p>
          </div>
        </section>
      )}
    </Dropzone>)
}
