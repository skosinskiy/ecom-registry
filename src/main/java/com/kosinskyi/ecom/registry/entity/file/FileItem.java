package com.kosinskyi.ecom.registry.entity.file;

import com.kosinskyi.ecom.registry.entity.base.BaseEntity;
import com.kosinskyi.ecom.registry.entity.file.listener.FileItemListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

@Entity
@Table(name = "file_item")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@EntityListeners(FileItemListener.class)
public class FileItem extends BaseEntity {

  @Column(name = "file_key", nullable = false)
  private String fileKey;

  @Column(name = "size", nullable = false)
  private Long size;

}
