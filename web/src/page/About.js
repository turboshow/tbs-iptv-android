import React from 'react';
import { useTranslation } from 'react-i18next';
import { Layout } from 'antd';

const { Content } = Layout;

function About() {
  const { t } = useTranslation();
  return (
    <Content style={{ padding: '20px', backgroundColor: 'white' }}>
      <div>{t('version')}: v0.2.4</div>
    </Content>
  );
}

export default About;