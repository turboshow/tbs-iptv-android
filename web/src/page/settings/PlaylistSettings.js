import { Button, Icon, message, Upload } from 'antd';
import React from 'react';

function PlaylistSettings() {
  const props = {
    name: 'playlist',
    action: '/api/settings/playlist',
    showUploadList: false,
    onChange(info) {
      if (info.file.status !== 'uploading') {
        console.log(info.file, info.fileList);
      }
      if (info.file.status === 'done') {
        message.success('上传成功');
      } else if (info.file.status === 'error') {
        message.error('上传失败, 请检查文件格式是否正确');
      }
    },
  };

  return (
    <div style={{ padding: '20px' }}>
      <Upload {...props}>
        <Button>
          <Icon type="upload" />点击上传频道列表
        </Button>
      </Upload>
    </div>
  );
}

export default PlaylistSettings;