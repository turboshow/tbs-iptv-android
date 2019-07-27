import React from 'react';
import { Layout } from 'antd';

const { Content } = Layout;

function About() {
  return (
    <Content style={{ padding: '20px', backgroundColor: 'white' }}>
      <div>版本: v0.1.1</div>
    </Content>
  );
}

export default About;