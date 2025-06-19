module.exports = {
  preset: 'ts-jest',
  testEnvironment: 'jest-environment-jsdom',  
  setupFilesAfterEnv: ['<rootDir>/setupTests.ts'],
  moduleNameMapper: {
    '\\.(css|s[ac]ss)$': 'identity-obj-proxy'
  },
};
