import { Button, Form, Input, message, Radio } from 'antd';
import React, { useEffect } from 'react';
import { fetchUdpxySettings, updateUdpxySettings } from '../../api';
import { useTranslation } from 'react-i18next';

function UdpxySettings({ form }) {
  const { t } = useTranslation();
  const { getFieldDecorator, setFieldsValue, getFieldValue, validateFields } = form;
  const formItemLayout = {
    labelCol: { span: 2 },
    wrapperCol: { span: 14 },
  };

  useEffect(() => {
    fetchUdpxySettings().then(settings => {
      setFieldsValue({
        enable: settings.addr != null,
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
        }).then(() => message.success(t('udpxy.success')), () => message.error(t('udpxy.error')));
      }
    })
  }

  return (
    <Form {...formItemLayout} onSubmit={handleSubmit}>
      <Form.Item label={t('udpxy.enable')}>
        {getFieldDecorator('enable', {
          rules: [{
            required: true,
            message: t('udpxy.enableErrorMsg')
          }]
        })(
          <Radio.Group>
            <Radio value={true}>{t('udpxy.yes')}</Radio>
            <Radio value={false}>{t('udpxy.no')}</Radio>
          </Radio.Group>
        )}
      </Form.Item>
      <Form.Item label={t('udpxy.address')}>
        {getFieldDecorator('addr', {
          rules: [{
            required: getFieldValue('enable') === true,
            message: t('udpxy.addressErrorMsg')
          }]
        })(
          <Input placeholder="192.168.1.254:1212" disabled={!getFieldValue('enable')} />
        )}
      </Form.Item>
      <Form.Item wrapperCol={{ span: 2, offset: 2 }}>
        <Button type="primary" htmlType="submit">
          {t('udpxy.save')}
        </Button>
      </Form.Item>
    </Form>
  );
};

export default Form.create()(UdpxySettings);