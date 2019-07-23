import { Button, Form, Input, message, Radio } from 'antd';
import React, { useEffect } from 'react';
import { fetchUdpxySettings, updateUdpxySettings } from '../../api';

function UdpxySettings({ form }) {
  const { getFieldDecorator, setFieldsValue, getFieldValue, validateFields } = form;
  const formItemLayout = {
    labelCol: { span: 2 },
    wrapperCol: { span: 14 },
  };

  useEffect(() => {
    fetchUdpxySettings().then(settings => {
      setFieldsValue({
        enable: settings.addr !== null,
        addr: settings.addr
      });
    })
  }, [setFieldsValue]);

  const handleSubmit = e => {
    e.preventDefault();
    validateFields(error => {
      if (!error) {
        updateUdpxySettings({
          addr: (form.getFieldValue('enable') && form.getFieldValue('addr')) || null
        }).then(() => message.success('保存成功'), () => message.error('保存失败'));
      }
    })
  }

  return (
    <Form {...formItemLayout} onSubmit={handleSubmit}>
      <Form.Item label="开启">
        {getFieldDecorator('enable', {
          rules: [{
            required: true,
            message: '请选择'
          }]
        })(
          <Radio.Group>
            <Radio value={true}>是</Radio>
            <Radio value={false}>否</Radio>
          </Radio.Group>
        )}
      </Form.Item>
      <Form.Item label="地址">
        {getFieldDecorator('addr', {
          rules: [{
            required: getFieldValue('enable') === true,
            message: '地址不能为空'
          }]
        })(
          <Input placeholder="192.168.1.254:1212" disabled={!getFieldValue('enable')} />
        )}
      </Form.Item>
      <Form.Item wrapperCol={{ span: 2, offset: 2 }}>
        <Button type="primary" htmlType="submit">
          保存
          </Button>
      </Form.Item>
    </Form>
  );
};

export default Form.create()(UdpxySettings);