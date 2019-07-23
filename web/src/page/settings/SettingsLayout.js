import { Layout, Menu } from 'antd';
import React, { useEffect, useState } from 'react';
import { Route, NavLink } from 'react-router-dom';
import PlaylistSettings from './PlaylistSettings';
import UdpxySettings from './UdpxySettings';

const { Sider, Content } = Layout;

function SettingsLayout({ location }) {
  const [activeMenuItem, setActiveMenuItem] = useState();

  useEffect(() => {
    setActiveMenuItem(location.pathname.split('/')[2]);
  }, [location]);

  return (
    <Layout>
      <Sider width={200} style={{ background: '#fff' }}>
        <Menu
          mode="inline"
          selectedKeys={[activeMenuItem]}
          style={{ height: '100%', paddingTop: '20px', borderRight: '1px solid #e8e8e8' }}
        >
          <Menu.Item key="playlist"><NavLink to="/settings/playlist">播放列表</NavLink></Menu.Item>
          <Menu.Item key="udpxy"><NavLink to="/settings/udpxy">udpxy</NavLink></Menu.Item>
        </Menu>
      </Sider>
      <Content style={{ backgroundColor: 'white', padding: '20px' }}>
        <Route path="/settings/playlist" component={PlaylistSettings} />
        <Route path="/settings/udpxy" component={UdpxySettings} />
      </Content>
    </Layout>
  );
};

export default SettingsLayout;