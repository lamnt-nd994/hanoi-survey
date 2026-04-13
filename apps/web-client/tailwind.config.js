/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,vue}'],
  theme: {
    extend: {
      fontFamily: {
        heading: ['Montserrat', 'sans-serif'],
        body: ['Open Sans', 'sans-serif']
      },
      colors: {
        primary: {
          navy: '#0F2744',
          dark: '#0A1A2D',
          light: '#1A3A5C'
        },
        accent: {
          green: '#2E7D32',
          dark: '#1B5E20',
          light: '#4CAF50'
        },
        neutral: {
          50: '#F4F6F8',
          100: '#D9DEE3',
          200: '#C5CDD4',
          300: '#9AA8B5',
          400: '#6B7C8F',
          500: '#4A5A6A',
          600: '#3D4A58',
          700: '#2D3744',
          800: '#1F2A36',
          900: '#141920'
        }
      },
      boxShadow: {
        panel: '0 4px 6px -1px rgba(15, 39, 68, 0.1), 0 2px 4px -1px rgba(15, 39, 68, 0.06)',
        'panel-lg': '0 10px 15px -3px rgba(15, 39, 68, 0.1), 0 4px 6px -2px rgba(15, 39, 68, 0.05)',
        glow: '0 0 20px rgba(46, 125, 50, 0.3)'
      },
      backgroundImage: {
        'hero-gradient': 'linear-gradient(135deg, rgba(15, 39, 68, 0.92) 0%, rgba(26, 58, 92, 0.88) 100%)',
        'pattern-dots': 'radial-gradient(circle, rgba(46, 125, 50, 0.1) 1px, transparent 1px)'
      },
      spacing: {
        '18': '4.5rem',
        '22': '5.5rem'
      }
    },
  },
  plugins: [],
}
