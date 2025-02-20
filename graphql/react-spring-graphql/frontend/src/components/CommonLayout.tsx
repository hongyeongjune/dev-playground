import { BackgroundProps, Box } from '@chakra-ui/react';
import { ReactNode } from 'react';
import Navbar from './nav/Navbar';

interface CommonLayoutProps {
    bg?: BackgroundProps['bg'];
    children: ReactNode;
}

const CommonLayout = ({bg, children}: CommonLayoutProps) => {
    return (
        <div>
            <Navbar />
            <Box
                px={{ base: 4 }}
                pt={24}
                mx="auto"
                maxW="960px"
                minH="100vh"
                w="100%"
                bg={bg}
            >
                {children}
            </Box>
        </div>
    );
}

export default CommonLayout;